package com.nerdstone.neatformcore.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nerdstone.neatformcore.domain.model.NFormViewData

/**
 * Created by cozej4 on 2019-11-25.
 *
 *@cozej4 https://github.com/cozej4
 */

class DataViewModel : ViewModel() {
    var details: MutableLiveData<HashMap<String, NFormViewData>> = MutableLiveData(HashMap())

    fun saveValue(fieldName: String, fieldValue:NFormViewData){
        details.value?.set(fieldName, fieldValue)
    }
}