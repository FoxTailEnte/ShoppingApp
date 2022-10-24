package com.example.shopinglist.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.example.shopinglist.R
import com.example.shopinglist.databinding.NewListDialogBinding

object NewListDialog {
    fun showDialog(contex: Context, listener: Listener, name: String){
        var dialog: AlertDialog? = null
        val builder= AlertDialog.Builder(contex)
        val binding = NewListDialogBinding.inflate(LayoutInflater.from(contex))
        builder.setView(binding.root)
        binding.apply {
            edNewListName.setText(name)
            if(name.isNotEmpty()) bCreate.text=contex.getString(R.string.update)
            bCreate.setOnClickListener{
                val listName = edNewListName.text.toString()
                if(listName.isNotEmpty()){
                    listener.onClick(listName)
                }
                    dialog?.dismiss()
            }
        }
        dialog = builder.create()
        dialog.window?.setBackgroundDrawable(null)
        dialog.show()
    }
    interface Listener{
        fun onClick(name:String)
    }
}