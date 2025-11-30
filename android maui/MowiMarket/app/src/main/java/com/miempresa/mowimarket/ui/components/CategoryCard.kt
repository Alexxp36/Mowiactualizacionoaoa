package com.miempresa.mowimarket.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.miempresa.mowimarket.data.models.Category
import com.miempresa.mowimarket.ui.theme.CategoryBackground
import com.miempresa.mowimarket.ui.theme.TextWhite

@Composable
fun CategoryCard(
    category: Category,
    onClick: (Category) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(150.dp)
            .clickable { onClick(category) },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = CategoryBackground
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Icon Box
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(
                        color = CategoryBackground.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = getCategoryIcon(category.iconName),
                    contentDescription = category.name,
                    modifier = Modifier.size(40.dp),
                    tint = TextWhite
                )
            }

            // Category Name
            Text(
                text = category.name,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = TextWhite,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun getCategoryIcon(iconName: String): ImageVector {
    return when (iconName) {
        "smartphone" -> Icons.Default.Smartphone
        "checkroom" -> Icons.Default.Checkroom
        "home" -> Icons.Default.Home
        "pets" -> Icons.Default.Pets
        "child_care" -> Icons.Default.ChildCare
        "sports_esports" -> Icons.Default.SportsEsports
        else -> Icons.Default.Category
    }
}
