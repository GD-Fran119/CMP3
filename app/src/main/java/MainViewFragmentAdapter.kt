import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.cmp3.PlaylistListView
import com.example.cmp3.SongListView

/**
 * Adapter for view pager in Main Activity
 * @param fragmentManager manager required by [FragmentStateAdapter]
 * @param lifecycle required by [FragmentStateAdapter]
 */
class MainViewFragmentAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle)
{
    private val fragments = arrayOf(SongListView.newInstance(), PlaylistListView.newInstance())
    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

}