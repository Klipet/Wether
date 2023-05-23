package com.example.wether

import android.app.AlertDialog
import android.content.Context
import android.widget.EditText

object DialogMenager {
    fun locationSettingsDialog(context: Context, listener: Listener){
        val builder = AlertDialog.Builder(context)
        val dialog = builder.create()
        dialog.setTitle("Enable Location?")
        dialog.setMessage("Location disabled, doo you want enable location?")
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok"){_,_ ->
            listener.onClick(null)
            dialog.dismiss()
        }
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel"){_,_ ->
            dialog.dismiss()
        }
        dialog.show()

    }

    fun serchByNameDialog(context: Context, listener: Listener){
        val builder = AlertDialog.Builder(context)
        val edName = EditText(context)
        builder.setView(edName)
        val dialog = builder.create()
        dialog.setTitle("City Name")
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok"){_,_ ->
            listener.onClick(edName.text.toString())
            dialog.dismiss()
        }
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel"){_,_ ->
            dialog.dismiss()
        }
        dialog.show()

    }
    interface Listener{
        fun onClick(name: String?)
    }
}