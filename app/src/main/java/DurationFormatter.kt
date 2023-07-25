class DurationFormatter{
    companion object{
        fun format(time: Long): String{
            val sec = (time / 1000) % 60
            val min = (time / 60000) % 60
            val hour = time / 3600000

            var formatted = if(sec < 10) ":0$sec" else ":$sec"
            formatted = (if(min < 10) "0$min" else min.toString()) + formatted
            formatted = when{
                hour == 0L -> formatted
                hour < 10L -> "0$hour:$formatted"
                else -> "$hour:$formatted"
            }

            return formatted
        }
    }
}