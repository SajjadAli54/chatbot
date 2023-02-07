package com.example.chatbot

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class MessageRVAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var messageList: ArrayList<MessageModel>
    lateinit var context: Context

    constructor(messageModalArrayList: ArrayList<MessageModel>, context: Context) : this() {
        this.messageList = messageModalArrayList
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view: View
        return when(viewType){
            0 -> {
                view = LayoutInflater.from(context).inflate(R.layout.user_msg, parent, false)
                UserViewHolder(view)
            }
            else -> {
                view = LayoutInflater.from(context).inflate(R.layout.bot_msg, parent, false)
                BotViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = messageList[position]
        when (model.sender) {
            "user" ->{
                val user = holder as UserViewHolder
                user.userTV.text = model.message
            }
            "bot" -> {
                val bot = holder as BotViewHolder
                bot.botTV.text = model.message
            }
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun getItemViewType(position: Int): Int {
        // below line of code is to set position.
        return when (messageList.get(position).sender) {
            "user" -> 0
            "bot" -> 1
            else -> -1
        }
    }

    class UserViewHolder(view: View): RecyclerView.ViewHolder(view){
        var userTV: TextView = view.findViewById(R.id.idTVUser)
    }

    class BotViewHolder(view: View): RecyclerView.ViewHolder(view){
        var botTV: TextView = view.findViewById(R.id.idTVBot)
    }

}