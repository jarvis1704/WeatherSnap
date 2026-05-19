package com.biprangshu.weathersnap.feature.weather.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.biprangshu.weathersnap.ui.theme.DarkOliveText
import com.biprangshu.weathersnap.ui.theme.DarkSurface
import com.biprangshu.weathersnap.ui.theme.DarkSurfaceVariant
import com.biprangshu.weathersnap.ui.theme.LimeAccent
import com.biprangshu.weathersnap.ui.theme.OnDarkSurface
import com.biprangshu.weathersnap.ui.theme.OnDarkSurfaceVariant

@Composable
fun SearchCard(
    query: String,
    isLoading: Boolean,
    onQueryChange: (String) -> Unit,
    onSearchClick: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                OutlinedTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    modifier = Modifier.weight(1f),
                    label = { Text("City") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LimeAccent,
                        unfocusedBorderColor = OnDarkSurfaceVariant,
                        focusedLabelColor = LimeAccent,
                        unfocusedLabelColor = OnDarkSurfaceVariant,
                        focusedTextColor = OnDarkSurface,
                        unfocusedTextColor = OnDarkSurface,
                        cursorColor = LimeAccent,
                    ),
                    shape = RoundedCornerShape(12.dp),
                )
                Button(
                    onClick = onSearchClick,
                    enabled = !isLoading,
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LimeAccent,
                        contentColor = DarkOliveText,
                        disabledContainerColor = DarkSurfaceVariant,
                        disabledContentColor = OnDarkSurfaceVariant,
                    ),
                    modifier = Modifier.height(56.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp),
                ) {
                    Text(
                        text = if (isLoading) "..." else "Search",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Enter more than 2 letters to start city suggestions.",
                style = MaterialTheme.typography.bodySmall,
                color = OnDarkSurfaceVariant,
            )
        }
    }
}
