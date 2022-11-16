package com.group.daily_forecast.utils

import android.content.Context
import android.util.Log
import com.group.daily_forecast.R

sealed class NetworkState {

    //idle
    object Idle : NetworkState()

    //loading
    object Loading : NetworkState()

    //result
    data class Result<T>(var response: T) : NetworkState()

    //error
    data class Error(var errorCode: Int, var msg: String? = null) : NetworkState() {

        fun handleErrors(
            mContext: Context,
        ) {

            Log.e(TAG, "handleErrors: msg $msg")
            Log.e(TAG, "handleErrors: error code $errorCode")

            when (errorCode) {

                Constants.Codes.EXCEPTIONS_CODE -> {
                    showMsg(
                        msg = if (msg.isNullOrEmpty()) mContext.getString(R.string.internet_connection) else msg!!,
                        mContext = mContext
                    )
                }
                Constants.Codes.X_API_KEY_CODE -> {
                    showMsg(
                        msg = if (msg.isNullOrEmpty()) mContext.getString(R.string.x_api_key_error) else msg!!,
                        mContext = mContext
                    )
                }
                Constants.Codes.AUTH_CODE -> {
                    showMsg(
                        msg = if (msg.isNullOrEmpty()) mContext.getString(R.string.auth_error) else msg!!,
                        mContext = mContext
                    )
                }
                Constants.Codes.UNKNOWN_CODE -> {

                    showMsg(
                        msg = /*if (msg.isNullOrEmpty())*/ mContext.getString(R.string.known_error) /*else msg*/,
                        mContext = mContext
                    )

                }
            }

        }

        private fun showMsg(
            mContext: Context,
            msg: String
        ) {
            Utils.showToast(mContext, msg)
        }

        companion object {
            private val TAG = this::class.java.name
        }

    }

}

