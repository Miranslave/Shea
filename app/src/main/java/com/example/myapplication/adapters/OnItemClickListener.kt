package com.example.myapplication.adapters

interface OnItemClickListener {
    fun onItemClick(position: Int, item: Any)
    fun onItemDraw(holder: WebtoonsRecyclerViewHolder, position: Int, item: Any)
}