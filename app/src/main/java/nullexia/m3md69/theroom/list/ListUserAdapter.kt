package nullexia.m3md69.theroom.list

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import nullexia.m3md69.theroom.R
import nullexia.m3md69.theroom.databinding.ListUserBinding
import nullexia.m3md69.theroom.room.model.User
import nullexia.m3md69.theroom.room.view_model.UserViewModel
import nullexia.m3md69.theroom.utilities.Constants

class ListUserAdapter : RecyclerView.Adapter<ListUserAdapter.ListUserHolder>, Filterable {

    private lateinit var binding: ListUserBinding

    private lateinit var mUserViewModel: UserViewModel

    private var mContext: Context

    var arrUser = ArrayList<User>()

    private var filterList: ArrayList<User>
    private var filter: ListUserFilter? = null

    constructor(mContext: Context, arrUser: ArrayList<User>) {
        this.mContext = mContext
        this.arrUser = arrUser
        this.filterList = arrUser
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListUserHolder {
        binding = ListUserBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return ListUserHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ListUserHolder, position: Int) {
        val userModel = arrUser[position]
        holder.id.text = userModel.id.toString()
        holder.fullName.text = userModel.fullName
        holder.age.text = userModel.age.toString()
        holder.job.text = userModel.job

        holder.itemView.setOnClickListener {
            val selectedUserId = userModel.id.toString()
            val bundle = Bundle().apply {
                putString(Constants.USER_ID, selectedUserId)
            }
            holder.itemView.findNavController().navigate(R.id.action_listUserFragment_to_addUserFragment, bundle)
        }

        holder.itemView.setOnLongClickListener {
            deleteUser(userModel, position)
            true
        }
    }

    override fun getItemCount(): Int = arrUser.size

    override fun getFilter(): Filter {
        if (filter == null) filter = ListUserFilter(this, filterList)
        return filter as ListUserFilter
    }

//    fun setData(user: List<User>) {
//        this.arrUser = user as ArrayList<User>
//        notifyDataSetChanged()
//    }

    private fun deleteUser(userModel: User, position: Int) {
        mUserViewModel = ViewModelProvider(mContext as AppCompatActivity)[UserViewModel::class.java]

        val builder = AlertDialog.Builder(mContext)

        val fullName = arrUser[position].fullName

        val textColor = Color.parseColor("#d0bcff")
        val coloredTitle = SpannableString("Delete $fullName?")
        coloredTitle.setSpan(ForegroundColorSpan(textColor), 7, 7 + fullName.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        builder
            .setTitle(coloredTitle)
            .setMessage("Are you sure you want to delete?")
            .setPositiveButton("Yes") { dialog, which ->
                mUserViewModel.deleteUser(userModel)
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, which -> dialog.dismiss() }
            .setOnCancelListener { it.dismiss() }
            .show()
    }

    inner class ListUserHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val id = binding.id
        val fullName = binding.fullName
        val age = binding.age
        val job = binding.job
    }
}