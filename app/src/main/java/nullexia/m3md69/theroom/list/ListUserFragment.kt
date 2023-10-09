package nullexia.m3md69.theroom.list

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import nullexia.m3md69.theroom.R
import nullexia.m3md69.theroom.databinding.FragmentListUserBinding
import nullexia.m3md69.theroom.room.UserViewModel

@AndroidEntryPoint
class ListUserFragment : Fragment() {

    private lateinit var binding: FragmentListUserBinding
    private lateinit var mUserViewModel: UserViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentListUserBinding.inflate(layoutInflater)

        mUserViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        setListeners()
        loadUsers()

        return binding.root
    }

    private var openMenu: Boolean = true

    private fun setListeners() {
        binding.justAddUserBtn.setOnClickListener { findNavController().navigate(R.id.action_listUserFragment_to_addUserFragment) }
        binding.justDeleteAllUsersBtn.setOnClickListener { showDeleteAllUsersDialog() }
        binding.justMenuBtn.setOnClickListener { setupFabMenu() }
        binding.justCloseMenuBtn.setOnClickListener { setupFabMenu() }
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
            binding.justDeleteAllUsersBtn.show()
            false
        } else {
            binding.justMenuBtn.show()
            binding.justCloseMenuBtn.hide()
            binding.justAddUserBtn.hide()
            binding.justDeleteAllUsersBtn.hide()
            true
        }
    }

    private fun loadUsers() {
        val myUsersAdapter = ListUserAdapter(requireContext())
        val myUsersRecyclerView = binding.justMyUsers
        myUsersRecyclerView.adapter = myUsersAdapter
        mUserViewModel.readAllData.observe(viewLifecycleOwner) { user -> myUsersAdapter.setData(user) }
    }

}