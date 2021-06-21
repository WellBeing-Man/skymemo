package com.ldg.skymemo.list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ldg.skymemo.data.Memo
import com.ldg.skymemo.data.MemoFile
import com.ldg.skymemo.repository.ReadRepository
import com.ldg.skymemo.util.ListLiveData
import com.ldg.skymemo.util.mapToMemo
import com.ldg.skymemo.util.mapToMemoFile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.ObjectInputStream
import javax.inject.Inject

@Suppress("BlockingMethodInNonBlockingContext")
@HiltViewModel
class ListViewModel @Inject constructor(memoReadRepository: ReadRepository) : ViewModel() {

    private val DEBUG_TAG="ListViewModel"

    private val _addButtonClicked=MutableLiveData<Boolean>().apply { value=false}
    val addButtonClicked:LiveData<Boolean>
        get() = _addButtonClicked


    private val _refreshList=MutableLiveData<Boolean>().apply { value=false}
    val refreshList:LiveData<Boolean>
        get() = _refreshList

    private val fileMemo= mutableMapOf<Memo,File>()

    val memoList=ListLiveData<Memo>()



    init {
        memoReadRepository.read()
    }

    fun onRefreshList(){
        _refreshList.value=true
    }

    fun onRefreshListDone(){
        _refreshList.value=false
    }

    fun onClickAddButton(){
        _addButtonClicked.value=true
    }

    fun onClickAddButtonDone(){
        _addButtonClicked.value=false
    }

    fun addMemo(fileList: Array<File>) {
        Log.d(DEBUG_TAG,fileList.size.toString())

        viewModelScope.launch {
            memoList.clear(false)
            for (file in fileList) {

                if(fileMemo.contains(file).not()) {
                    val memoFile = file.mapToMemoFile();
                    memoFile ?: return@launch
                    val memo = memoFile.mapToMemo();
                    fileMemo[memo]=file
                    memoList.add(memo)
                    memoList.notifyChange()
                }
            }

        }
    }



    fun remove(memo: Memo) {
        val file=fileMemo[memo]

        viewModelScope.launch {
            try {
                file?:return@launch

                if(file.delete()) {
                    memoList.remove(memo)
                    memoList.notifyChange()
                }
            }catch (f:NoSuchFileException){
                f.printStackTrace()
            }
        }
    }
}