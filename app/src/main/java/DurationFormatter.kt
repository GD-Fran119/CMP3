/**
 * Class for formatting time to string
 */
class DurationFormatter{
    companion object{
        /**
         * Formats [time] as [String]
         * @param [time] time in millis to format
         * @return [String] in format HH:MM:SS, MM:SS and SSs/Ss (e.g. 14s, 3s) depending on the time to format. This is:
         * - If [time] is lower than 1 minute, SSs format is returned
         * - If [time] is between [1, 60) minutes, MM:SS format is returned
         * - In any other case, HH:MM:SS format is returned
         */
        fun format(time: Long): String{
            val sec = (time / 1000) % 60
            val min = (time / 60000) % 60
            val hour = time / 3600000

            var formatted = ""

            if(hour == 0L && min == 0L)
                formatted += "${sec}s"
            else{
                if(hour > 0)
                    formatted += if(hour < 10) "0$hour:"
                                else "$hour:"

                if(min > 0)
                    formatted += if(min < 10) "0$min:"
                                else "$min:"

                formatted += if(sec < 10) "0$sec"
                            else "$sec"
            }

            return formatted
        }
    }
}