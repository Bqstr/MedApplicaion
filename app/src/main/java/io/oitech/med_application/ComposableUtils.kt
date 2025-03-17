package io.oitech.med_application

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

object ComposableUtils {


    @Composable
    fun ColumnScope.Space(dp:Dp){
        Spacer(modifier = Modifier.height(dp))
    }
    @Composable
    fun RowScope.Space(dp:Dp){
        Spacer(modifier = Modifier.width(dp))
    }

    @Composable
    fun RowScope.Space(){
        Spacer(modifier = Modifier.weight(1f))
    }


    @Composable
    fun ColumnScope.Space(){
        Spacer(modifier = Modifier.weight(1f))
    }


}