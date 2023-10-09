package nullexia.m3md69.theroom.submit

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import nullexia.m3md69.theroom.R
import nullexia.m3md69.theroom.databinding.FragmentSubmitUserBinding
import nullexia.m3md69.theroom.room.UserViewModel
import nullexia.m3md69.theroom.room.model.User
import nullexia.m3md69.theroom.utilities.Constants

class SubmitUserFragment : Fragment() {

    private lateinit var binding: FragmentSubmitUserBinding
    private lateinit var mUserViewModel: UserViewModel
    private var isEdit = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSubmitUserBinding.inflate(layoutInflater)

        mUserViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        setListeners()

        checkEdit()

        return binding.root
    }

    private fun setListeners() = binding.justSubmit.setOnClickListener { addOrUpdate() }

    private fun inputCheck(fullName: String, age: String, job: String): Boolean = !(TextUtils.isEmpty(fullName) && TextUtils.isEmpty(age) && TextUtils.isEmpty(job))

    private fun checkEdit() {
        val userId = arguments?.getString(Constants.USER_ID)
        isEdit = userId != null
        if (isEdit) {
            binding.justSubmit.text = getString(R.string.update)
            binding.justId.isVisible = true

            mUserViewModel.getUserById(userId!!.toInt()).observe(viewLifecycleOwner) { user ->
                user?.let {
                    binding.justId.text = it.id.toString()
                    binding.justFullName.setText(it.fullName)
                    binding.justAge.setText(it.age.toString())
                    binding.justJob.setText(it.job)
                }
            }
        } else {
            binding.justSubmit.text = getString(R.string.add)
            binding.justId.isVisible = false
            binding.justId.text = null
        }
    }

    private fun addOrUpdate() {
        val userId = arguments?.getString(Constants.USER_ID)
        isEdit = userId != null
        if (isEdit) updateDataInDatabase(userId!!.toInt())
        else insertDataToDatabase()
    }

    private fun insertDataToDatabase() {
        val fullName = binding.justFullName.text.toString()
        val age = binding.justAge.text.toString()
        val job = binding.justJob.text.toString()
        var theUnusedId = 1
        try {
            if (inputCheck(fullName, age, job)) {
                mUserViewModel.readAllData.observe(viewLifecycleOwner) { user ->
                    var newUser = User(theUnusedId, fullName, age.toInt(), job)
                    if (user.isNotEmpty())
                        while (theUnusedId <= user.size) {
                            ++theUnusedId
                            newUser = User(theUnusedId, fullName, age.toInt(), job)
                        }
                    Toast.makeText(requireContext(), "Done Add", Toast.LENGTH_SHORT).show()
                    mUserViewModel.addUser(newUser)
                    try {
                        findNavController().navigate(R.id.action_addUserFragment_to_listUserFragment)
                    } catch (_: Exception) {

                    }
                }
            } else Toast.makeText(requireContext(), "Error Add", Toast.LENGTH_SHORT).show()
        } catch (_: Exception) {
        }
    }

    private fun updateDataInDatabase(userId: Int) {
        val fullName = binding.justFullName.text.toString()
        val age = binding.justAge.text.toString()
        val job = binding.justJob.text.toString()

        if (inputCheck(fullName, age, job)) {
            val user = User(userId, fullName, age.toInt(), job)
            mUserViewModel.updateUser(user)
            Toast.makeText(requireContext(), "Done Update", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_addUserFragment_to_listUserFragment)
        } else Toast.makeText(requireContext(), "Error Update", Toast.LENGTH_SHORT).show()
    }

}