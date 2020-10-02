package com.akbarprojec.roomapp.fragments.update

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.akbarprojec.roomapp.R
import com.akbarprojec.roomapp.model.User
import com.akbarprojec.roomapp.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.fragment_update.*
import kotlinx.android.synthetic.main.fragment_update.view.*

class UpdateFragment : Fragment() {

    private val args by navArgs<UpdateFragmentArgs>()
    private lateinit var mUserViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_update, container, false)

        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

//      mem PASSING firstnam, lastname, age to fragmentUpdate with args currentUser for show data in fraagmentUpdaate
        view.updateFirstName_et.setText(args.currentUser.fristName)
        view.updateLastName_et.setText(args.currentUser.lastName)
        view.updateAge_et.setText(args.currentUser.Age.toString())

//        Set update_btn
        view.update_btn.setOnClickListener {
            updateItem()
        }

//        Add menu
        setHasOptionsMenu(true)

        return view
    }

    private fun updateItem(){
        val firstName = updateFirstName_et.text.toString()
        val lastName = updateLastName_et.text.toString()
        val age = Integer.parseInt(updateAge_et.text.toString())

        if (inputCheck(firstName, lastName, updateAge_et.text)){
//            create User Object
            val updatedUser = User(args.currentUser.id, firstName, lastName, age)

//            Update Current user
            mUserViewModel.updateUser(updatedUser)
            Toast.makeText(requireContext(), "Update Successfully!", Toast.LENGTH_LONG).show()
//            Navigate Back
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }else{
            Toast.makeText(requireContext(), "Please Fill out all fields.", Toast.LENGTH_LONG).show()
        }
    }

    private fun inputCheck(firstName: String, lastName: String, age: Editable): Boolean{
        return !(TextUtils.isEmpty(firstName) && TextUtils.isEmpty(lastName) && age.isEmpty())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_delete){
            deleteUser()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteUser() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_, _ ->
            mUserViewModel.deleteUser(args.currentUser)
            Toast.makeText(requireContext(), "Successfully removed: ${args.currentUser.fristName}",
            Toast.LENGTH_SHORT).show()

            findNavController().navigate(R.id.action_updateFragment_to_listFragment)

        }
        builder.setNegativeButton("No"){_, _ ->}
        builder.setTitle("Delete ${args.currentUser.fristName}?")
        builder.setMessage("Are you sure you want to delete ${args.currentUser.fristName}?")
        builder.create().show()
    }
}