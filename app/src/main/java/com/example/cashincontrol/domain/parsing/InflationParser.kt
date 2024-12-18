import android.content.Context
import co.yml.charts.common.model.Point
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.ss.util.CellRangeAddress
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Month
import java.util.Locale


//suspend fun parseInflation(context: Context): Map<LocalDate, Point> {
//    val fileUrl = "https://www.cbr.ru/vfs/regions/profile/RgInflation_1.xlsx"
//    val localFileName = "RgInflation_1.xlsx"
//
//    // Скачиваем файл в внутреннее хранилище приложения
//    downloadFile(context, fileUrl, localFileName)
//
//    // Читаем последние 12 значений
//    val filePath = File(context.filesDir, localFileName).absolutePath
//    val points = readLast12InflationData(filePath, "Свердловская область")
//
//    println("Собранные данные:")
//    if (points.isEmpty()) {
//        println("Массив точек пустой!")
//    } else {
//        points.forEach { (date, point) -> println("Дата: $date, Точка: $point") }
//    }
//    return points
//}
//
//suspend fun downloadFile(context: Context, url: String, outputFileName: String) {
//    withContext(Dispatchers.IO) { // Выполняем в фоновом потоке
//        val inputStream = URL(url).openStream()
//        val file = File(context.filesDir, outputFileName) // Сохраняем во внутреннем хранилище
//
//        FileOutputStream(file).use { outputStream ->
//            inputStream.use { input ->
//                input.copyTo(outputStream)
//            }
//        }
//        println("Файл успешно загружен: ${file.absolutePath}")
//    }
//}




suspend fun parseInflation(context: Context):  Map<LocalDate, Point> {
    val fileUrl = "https://www.cbr.ru/vfs/regions/profile/RgInflation_1.xlsx"
    val localFileName = "RgInflation_1.xlsx"

    // Загрузка файла
    val filePath = downloadFile(context, fileUrl, localFileName)

    // Чтение данных
    val points = readLast12InflationData(filePath, "Свердловская область")

    println("Собранные данные:")
    if (points.isEmpty()) {
        println("Массив точек пустой!")
    } else {
        points.forEach { (date, point) ->
            println("Дата: $date, Точка: $point")
        }
    }
    return points
}

suspend fun downloadFile(context: Context, url: String, outputFileName: String): String {
    return withContext(Dispatchers.IO) { // Выполняем в фоновом потоке
        val file = File(context.filesDir, outputFileName) // Путь к файлу во внутреннем хранилище
        URL(url).openStream().use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }
        println("Файл успешно загружен: ${file.absolutePath}")
        file.absolutePath // Возвращаем путь к файлу
    }
}

fun getMergedCellValue(row: Int, col: Int, sheet: org.apache.poi.ss.usermodel.Sheet): String? {
    for (mergedRegion in sheet.mergedRegions) {
        if (mergedRegion.isInRange(row, col)) {
            val firstRow = mergedRegion.firstRow
            val firstCol = mergedRegion.firstColumn
            return sheet.getRow(firstRow)?.getCell(firstCol)?.toString()
        }
    }
    return null
}

fun getMonthEnum(month: String): Month {
    return when (month.lowercase(Locale("ru"))) {
        "января" -> Month.JANUARY
        "февраля" -> Month.FEBRUARY
        "марта" -> Month.MARCH
        "апреля" -> Month.APRIL
        "мая" -> Month.MAY
        "июня" -> Month.JUNE
        "июля" -> Month.JULY
        "августа" -> Month.AUGUST
        "сентября" -> Month.SEPTEMBER
        "октября" -> Month.OCTOBER
        "ноября" -> Month.NOVEMBER
        "декабря" -> Month.DECEMBER
        else -> throw IllegalArgumentException("Неизвестный месяц: $month")
    }
}

fun readLast12InflationData(filePath: String, targetRegion: String): Map<LocalDate, Point> {
    val file = File(filePath)
    val workbook = WorkbookFactory.create(file)
    val sheet = workbook.getSheetAt(0)

    val points = mutableMapOf<LocalDate, Point>()
    val totalColumns = 12

    val monthRow = sheet.getRow(2)
    val yearRow = 1

    for (row in sheet) {
        val regionCell = row.getCell(0)
        if (regionCell != null && regionCell.toString().contains(targetRegion, ignoreCase = true)) {
            println("Обработка данных для региона: ${regionCell.toString()}")

            val lastColumnIndex = row.lastCellNum - 1
            val firstColumnIndex = lastColumnIndex - totalColumns + 1

            for ((i, col) in (firstColumnIndex..lastColumnIndex).withIndex()) {

                val monthRaw = monthRow.getCell(col)?.let { cell ->
                    when (cell.cellType) {
                        org.apache.poi.ss.usermodel.CellType.NUMERIC -> {
                            if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
                                val date = cell.dateCellValue
                                val sdf = SimpleDateFormat("MMMM", Locale("ru")) // Формат месяца
                                sdf.format(date) // Преобразование даты в название месяца
                            } else {
                                cell.numericCellValue.toString()
                            }
                        }
                        org.apache.poi.ss.usermodel.CellType.STRING -> cell.stringCellValue.trim()
                        else -> null
                    }
                }

                val yearRaw = getMergedCellValue(yearRow, col, sheet)?.replace("год", "")?.trim()

                if (monthRaw != null && yearRaw != null) {
                    try {
                        val month = getMonthEnum(monthRaw)
                        val year = yearRaw.toInt()
                        val inflationCell = row.getCell(col)

                        if (inflationCell != null) {
                            val date = LocalDate.of(year, month, 1)
                            val point = Point(x = (i + 1).toFloat(), y = inflationCell.numericCellValue.toFloat())
                            points[date] = point
                        }
                    } catch (e: Exception) {
                        println("Ошибка при создании даты: месяц=$monthRaw, год=$yearRaw")
                    }
                }
            }
            break
        }
    }
    workbook.close()
    return points
}
