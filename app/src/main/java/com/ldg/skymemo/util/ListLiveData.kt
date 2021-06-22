package com.ldg.skymemo.util

import androidx.lifecycle.MutableLiveData

//LiveData를 list형태로 사용하기 위한 Util
class ListLiveData<T> : MutableLiveData<ArrayList<T>> {

    constructor(){
        value=ArrayList<T>()
    }

    fun add(item:T){
        val items=value
        items!!.add(item)
        value=items
    }

    fun addAll(item:List<T>){
        val items=value
        items!!.addAll(item)
        value=items
    }

    fun clear(notify:Boolean){
        val items=value
        items!!.clear()
        if(notify)
            value=items
    }

    fun remove(item: T){
        val items=value
        items!!.remove(item)
        value=items
    }

    fun remove(index:Int){
        val items=value
        if(index<0 || items?.size!!<=index)
           return
        items.removeAt(index)
        value=items
    }

    fun notifyChange(){
        val items=value
        value=items
    }

    fun size():Int{
        val items=value
        items?: return 0
        return items.size
    }

    fun contains(item: T): Boolean {
        val items=value
        items?: return false
        return items.contains(item)
    }
}