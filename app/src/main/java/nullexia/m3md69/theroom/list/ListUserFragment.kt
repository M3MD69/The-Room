package nullexia.m3md69.theroom.list

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import nullexia.m3md69.theroom.R
import nullexia.m3md69.theroom.databinding.FragmentListUserBinding
import nullexia.m3md69.theroom.room.model.User
import nullexia.m3md69.theroom.room.view_model.UserViewModel

class ListUserFragment : Fragment() {

    private lateinit var binding: FragmentListUserBinding
    private lateinit var mUserViewModel: UserViewModel
    private lateinit var listUserAdapter: ListUserAdapter
    private lateinit var arrUser: ArrayList<User>
    private lateinit var mContext: Context
    override fun onAttach(context: Context) {
        this.mContext = context
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentListUserBinding.inflate(layoutInflater)

        mUserViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        arrUser = ArrayList()

        setListeners()
        loadUsers()

        return binding.root
    }

    private var openMenu: Boolean = true

    private fun setListeners() {
        binding.justAddUserBtn.setOnClickListener {
            findNavController().navigate(R.id.action_listUserFragment_to_addUserFragment)
            openMenu = true
        }
        binding.justBackupUsersBtn.setOnClickListener { Toast.makeText(mContext, "Soon", Toast.LENGTH_SHORT).show() }
        binding.justRestoreUsersBtn.setOnClickListener { Toast.makeText(mContext, "Soon", Toast.LENGTH_SHORT).show() }
        binding.justDeleteAllUsersBtn.setOnClickListener {
            showDeleteAllUsersDialog()
            openMenu = true
        }
        binding.justMenuBtn.setOnClickListener { setupFabMenu() }
        binding.justCloseMenuBtn.setOnClickListener { setupFabMenu() }
        setupSearch()
    }

    private fun setupSearch() {
        binding.justSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                try {
                    val query = s.toString()
                    if (query != "") {
                        mUserViewModel.readAllData.observe(viewLifecycleOwner) { users ->
                            arrUser.clear()
                            arrUser.addAll(users)
                        }
                        listUserAdapter.filter.filter(query)
                    } else {
                        mUserViewModel.readAllData.observe(viewLifecycleOwner) { users ->
                            arrUser.clear()
                            arrUser.addAll(users)
                            listUserAdapter.notifyDataSetChanged()
                        }
                    }
                } catch (_: Exception) {
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun showDeleteAllUsersDialog() {
        val builder = AlertDialog.Builder(requireContext())

        val textColor = Color.parseColor("#d0bcff")
        val coloredTitle = SpannableString("Delete All Users?")
        coloredTitle.setSpan(ForegroundColorSpan(textColor), 7, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        builder
            .setTitle(coloredTitle)
            .setMessage("Are you sure you want to delete?")
            .setPositiveButton("Yes") { dialog, which ->
                mUserViewModel.deleteAllUsers()
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, which -> dialog.dismiss() }
            .setOnCancelListener { it.dismiss() }
            .show()
    }

    private fun setupFabMenu() {
        openMenu = if (openMenu) {
            binding.justMenuBtn.hide()
            binding.justCloseMenuBtn.show()
            binding.justAddUserBtn.show()
            binding.justBackupUsersBtn.show()
            binding.justRestoreUsersBtn.show()
            binding.justDeleteAllUsersBtn.show()
            false
        } else {
            binding.justMenuBtn.show()
            binding.justCloseMenuBtn.hide()
            binding.justAddUserBtn.hide()
            binding.justBackupUsersBtn.hide()
            binding.justRestoreUsersBtn.hide()
            binding.justDeleteAllUsersBtn.hide()
            true
        }
    }

    private fun loadUsers() {
        listUserAdapter = ListUserAdapter(mContext, arrUser)
        binding.justMyUsers.adapter = listUserAdapter
        mUserViewModel.readAllData.observe(viewLifecycleOwner) { users ->
            arrUser.clear()
            arrUser.addAll(users)
            listUserAdapter.notifyDataSetChanged()
        }
    }

//    private fun loadUsers() {
//        val myUsersAdapter = ListUserAdapter(mContext, arrUser)
//        mUserViewModel.readAllData.observe(viewLifecycleOwner) { user -> myUsersAdapter.setData(user) }
//    }
}