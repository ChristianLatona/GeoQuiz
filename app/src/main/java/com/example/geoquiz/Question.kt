package com.example.geoquiz

import androidx.annotation.StringRes

//string res è opzionale, ma rende piu leggibile e previene crashes quando il costruttore non usa un valido resID
data class Question(@StringRes val textResID:Int, val answer:Boolean, var alreadyReplied:Boolean=false, var cheated:Boolean=false) /*: Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(textResID)
        parcel.writeByte(if (answer) 1 else 0)
        parcel.writeByte(if (alreadyReplied) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Question> {
        override fun createFromParcel(parcel: Parcel): Question {
            return Question(parcel)
        }

        override fun newArray(size: Int): Array<Question?> {
            return arrayOfNulls(size)
        }
    }
}*/
