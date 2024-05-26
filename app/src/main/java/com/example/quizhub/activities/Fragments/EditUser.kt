package com.example.quizhub.activities.Fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.quizhub.R
import com.example.quizhub.activities.models.User
import com.example.quizhub.databinding.UserBottomsheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log

class EditUser : BottomSheetDialogFragment() {
    private lateinit var binding: UserBottomsheetBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var userGender: String

    interface OnProfileUpdatedListener {
        fun onProfileUpdated()
    }

    private var listener: OnProfileUpdatedListener? = null

    companion object {
        private const val ARG_DATA = "arg_data"

        fun newInstance(data: User): EditUser {
            val args = Bundle()
            args.putParcelable(ARG_DATA, data)
            val fragment = EditUser()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnProfileUpdatedListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnProfileUpdatedListener")
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = UserBottomsheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Retrieve the User object from the arguments
        val user = arguments?.getParcelable<User>(ARG_DATA)

        // Set the initial values of the UI elements
        binding.etUserName.setText(user?.userName)
        userGender = user?.userGender ?: "Male"
        when (userGender) {
            "Male" -> {
                binding.radioMale.isChecked = true
                binding.profileImage2.setImageResource(R.drawable.profile)
            }

            "Female" -> {
                binding.radioFemale.isChecked = true
                binding.profileImage2.setImageResource(R.drawable.profile2)
            }

            else -> {
                binding.radioMale.isChecked = true
                binding.profileImage2.setImageResource(R.drawable.profile3)
            }
        }

        binding.radioGroupGender.setOnCheckedChangeListener { _, checkedId ->
            val selectedGender = when (checkedId) {
                R.id.radioMale -> {
                    binding.profileImage2.setImageResource(R.drawable.profile)
                    "Male"
                }

                R.id.radioFemale -> {
                    binding.profileImage2.setImageResource(R.drawable.profile2)
                    "Female"
                }

                else -> {
                    binding.profileImage2.setImageResource(R.drawable.profile3)
                    "Male"
                }
            }
            binding.radioGroupGender.tag = selectedGender
        }

        binding.btnSaveProfile.setOnClickListener {

            val name = binding.etUserName.text.toString().trim()
            val gender = binding.radioGroupGender.tag as? String ?: userGender

            if (name.isNotEmpty()) {
                val user1 = User(userName = name, userGender = gender)
                val uid = firebaseAuth.currentUser?.uid

                if (uid != null) {
                    firestore.collection("USERS").document(uid).set(user1)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(
                                    context,
                                    "Successfully Updated Profile",
                                    Toast.LENGTH_SHORT
                                ).show()
                                listener?.onProfileUpdated()
                                dismiss()
                            } else {
                                Toast.makeText(
                                    context,
                                    "Failed to Update Profile",
                                    Toast.LENGTH_SHORT
                                ).show()
                                Log.e("EditUser", "Failed to update profile: ${task.exception}")
                            }
                        }.addOnFailureListener { e ->
                            Toast.makeText(
                                context,
                                "Error: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.e("EditUser", "Error updating profile", e)
                        }
                } else {
                    Toast.makeText(context, "User is not authenticated", Toast.LENGTH_SHORT).show()
                    Log.e("EditUser", "User is not authenticated")
                }
            } else {
                Toast.makeText(context, "Please enter a name", Toast.LENGTH_SHORT).show()
                Log.e("EditUser", "Name is empty")
            }
        }
    }
}
