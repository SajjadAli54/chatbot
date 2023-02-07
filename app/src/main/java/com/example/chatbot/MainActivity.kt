package com.example.chatbot

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException


class MainActivity : AppCompatActivity() {
    private lateinit var chatsRV: RecyclerView
    lateinit var sendMsgIB: ImageButton
    lateinit var userMsgEdt: EditText

    var USER_KEY = "user"
    var BOT_KEY = "bot"

    lateinit var mRequestQueue: RequestQueue

    lateinit var messageModalArrayList: ArrayList<MessageModel>
    lateinit var messageRVAdapter: MessageRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chatsRV = findViewById(R.id.idRVChats);
        sendMsgIB = findViewById(R.id.idIBSend);
        userMsgEdt = findViewById(R.id.idEditMessage);

        mRequestQueue = Volley.newRequestQueue(this)
        mRequestQueue.cache.clear()

        messageModalArrayList = ArrayList()
        messageRVAdapter = MessageRVAdapter(messageModalArrayList, this)

        val linearLayoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        chatsRV.layoutManager = linearLayoutManager
        chatsRV.adapter = messageRVAdapter
    }

    fun handleSend(view: View){
        val text = userMsgEdt.text.toString()
        if (text.isEmpty()) {
            Toast.makeText(this, "Please enter your message..", Toast.LENGTH_SHORT).show();
            return;
        }
        sendMessage(text)

        userMsgEdt.setText("")
    }

    private fun sendMessage(userMsg: String){
        messageModalArrayList.add(MessageModel(userMsg, USER_KEY))
        messageRVAdapter.notifyDataSetChanged()

        val url ="http://api.brainshop.ai/get?bid=172514&key=n5LgcpsyAcEZ9SIG&uid=uid&msg=\"$userMsg\""

        val queue = Volley.newRequestQueue(this)

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                try {
                    val botResponse = response.getString("cnt")
                    messageModalArrayList.add(MessageModel(botResponse, BOT_KEY))
                    messageRVAdapter.notifyDataSetChanged()
                } catch (e: JSONException) {
                    e.printStackTrace()
                    messageModalArrayList.add(MessageModel("No response", BOT_KEY))
                    messageRVAdapter.notifyDataSetChanged()
                }
            })
            {
                messageModalArrayList.add(MessageModel("Sorry no response found", BOT_KEY))
                Toast.makeText(this, "No response from the bot..", Toast.LENGTH_SHORT).show()
            }
        queue.add(jsonObjectRequest)
    }
}