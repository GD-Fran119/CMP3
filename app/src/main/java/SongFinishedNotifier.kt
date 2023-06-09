import com.example.cmp3.UpdateUI

class SongFinishedNotifier private constructor() {
    companion object{
        fun notifySongChanged(){
            currentActivity?.updateUISongFinished()
        }
        private var currentActivity: UpdateUI? = null
        fun setCurrentActivity(activity: UpdateUI?){
            currentActivity = activity
        }
        fun getCurrentActivity(): UpdateUI?{
            return currentActivity
        }
    }
}