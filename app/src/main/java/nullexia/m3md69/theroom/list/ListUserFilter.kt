package nullexia.m3md69.theroom.list

import android.widget.Filter
import nullexia.m3md69.theroom.room.model.User
import java.util.Locale

class ListUserFilter(
    private val adapter: ListUserAdapter,
    private val filterList: ArrayList<User>,
) : Filter() {
    override fun performFiltering(constraint: CharSequence?): FilterResults {
        val results = FilterResults()

        if (!constraint.isNullOrEmpty()) {
            val query = constraint.toString().uppercase(Locale.getDefault())
            val filterModels = ArrayList<User>()

            for (user in filterList) {
                if (user.fullName.uppercase(Locale.getDefault()).contains(query)) {
                    filterModels.add(user)
                }
            }

            results.count = filterModels.size
            results.values = filterModels
        } else {
            results.count = filterList.size
            results.values = filterList
        }
        return results
    }

    override fun publishResults(constraint: CharSequence?, results: FilterResults) {
        adapter.arrUser.clear()
        adapter.arrUser.addAll(results.values as ArrayList<User>)
        adapter.notifyDataSetChanged()
    }
}