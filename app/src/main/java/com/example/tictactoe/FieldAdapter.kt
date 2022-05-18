package com.example.tictactoe

import Field
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import toSign


//ViewHolder - для каждого элемента списка создается объект, хранящий ссылки на отдельные View внутри элемента
//adapter - данные для обрисовки, которые размещаются по порядку в списке
//создаем свой адаптер для поля
class FieldAdapter(
    private val field: Field,
    private val onClick: (row: Int, col: Int) -> Unit
) : RecyclerView.Adapter<CustomViewHolder>() {

    init {
        setHasStableIds(true)   //каждой ячейке выделяется свой стабильный id
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {      //при создании
        return CustomViewHolder(parent, onClick)
    }

    override fun getItemId(position: Int): Long {       //получение id ячейки
        return position.toLong()
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {    //при получении
        val row = position / field.size //получение строки и столбца от позиции
        val col = position % field.size
        holder.bind(field.get(row, col), row, col)
    }

    override fun getItemCount(): Int = field.size * field.size      //количество элементов в сетке

}

//класс, создающий свой VH с Layout
class CustomViewHolder(parent: ViewGroup, onClick: (row: Int, col: Int) -> Unit) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.field_cell, parent, false)
) {
    private val markTV = itemView.findViewById<TextView>(R.id.mark)
    private var row: Int = -1
    private var col: Int = -1

    init {      //конструктор
        markTV.setOnClickListener {
            //  if (isFinished)
            onClick(row, col)
        }
    }

    fun bind(cell: Boolean?, row: Int, col: Int) {   //функция перевода значения ячейки в знак
        this.row = row
        this.col = col
        markTV.text = cell.toSign()
    }

}
//LayoutInflater - класс, создающий из layout-файла view-элемент
//resource - id файла, root - viewgroup-элемент для создаваемого view
//attachtoroot - присоединять ли новый view к родительскому

