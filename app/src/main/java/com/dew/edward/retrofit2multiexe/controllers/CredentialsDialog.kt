package com.dew.edward.retrofit2multiexe.controllers


import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.View
import com.dew.edward.retrofit2multiexe.R
import kotlinx.android.synthetic.main.dialog_credentials.view.*

/*
 * Created by Edward on 6/13/2018.
 */

class CredentialsDialog: DialogFragment() {

    lateinit var listener: ICredentialsDialogListener

    interface ICredentialsDialogListener{
        fun onDialogPositiveClick(username: String, password: String)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (activity is ICredentialsDialogListener) {
            listener = activity as ICredentialsDialogListener
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view: View = activity!!.layoutInflater.inflate(R.layout.dialog_credentials, null)

        view.editName.setText(arguments?.getString("username"))
        view.editPassword.setText(arguments?.getString("password"))

        return AlertDialog.Builder(activity as Context)
                .setView(view)
                .setTitle("Credentials")
                .setNegativeButton("Cancle", null)
                .setPositiveButton("Continue"){_, _ ->
                    if (listener != null){
                        listener.onDialogPositiveClick(view.editName.text.toString(),
                                view.editPassword.text.toString())
                    }
                }
                .create()
    }
}