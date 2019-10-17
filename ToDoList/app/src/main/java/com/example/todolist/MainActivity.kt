package com.example.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Adapter
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.io.FileNotFoundException
import java.io.PrintStream
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fillList()
    }

    private val toDoList: ArrayList<String> = ArrayList<String>()
    private var adapt :ArrayAdapter<String>? = null ;

    // notify changes to list
    // and persist new list to file
    private fun notifyDataChanged(){

        if(adapt == null) {
            adapt = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, toDoList)
            list.adapter = adapt
        }

        adapt?.notifyDataSetChanged()

        // save to file
        val out = PrintStream(openFileOutput("filename.txt", MODE_PRIVATE))
        for( x in toDoList){
            out.println(x)
        }
        out.close()
    }

    // fill and setup the list
    private fun fillList(){

        // read to-do list from file
        try {
            val scan = Scanner(openFileInput("filename.txt"))
            while (scan.hasNextLine()) {
                val line = scan.nextLine()
                toDoList.add(line)
            }
        }catch( e: FileNotFoundException){
            Log.wtf("exception","saved file doesn't exist/ can't be opened")
        }

        // if to-do list is empty add examples
        if(toDoList.size == 0){
            toDoList.add("Clean Kitchen")
            toDoList.add("Dust everywhere")
            toDoList.add("Scrub bathtub")
        }

        notifyDataChanged()

        // add remove and reorder click listeners
        list.setOnItemLongClickListener { parent, view, position, id ->
            val pos: Int = position
            toDoList.removeAt(pos)

            notifyDataChanged()
            true
        }

        list.setOnItemClickListener { parent, view, position, id ->
            val pos: Int = position
            val item = toDoList.removeAt(pos)
            toDoList.add(0, item)

            notifyDataChanged()
            true
        }
    }


    public fun addToList(view: android.view.View){
        toDoList.add(add_input.text.toString())
        add_input.setText("")
        notifyDataChanged()
    }


}
