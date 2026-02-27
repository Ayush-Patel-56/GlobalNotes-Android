package com.globalnotes.android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesListPanel(
    modifier: Modifier = Modifier,
    onNoteClick: () -> Unit = {},
    onMenuClick: () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    
    val filteredNotes by remember {
        derivedStateOf {
            if (searchQuery.text.isEmpty()) dummyNotes
            else dummyNotes.filter { it.title.contains(searchQuery.text, ignoreCase = true) }
        }
    }
    
    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.surface)
            .statusBarsPadding()
            .padding(24.dp)
            .navigationBarsPadding()
    ) {
        // Top Header
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    "Global Notes",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Text("All Notes", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            }
            IconButton(
                onClick = { /* New Note */ },
                modifier = Modifier.background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape)
            ) {
                Icon(Icons.Default.Add, contentDescription = "New", tint = MaterialTheme.colorScheme.primary)
            }
        }

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .height(52.dp),
            placeholder = { Text("Search your workspace...", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)) },
            leadingIcon = { 
                Icon(
                    Icons.Default.Search, 
                    contentDescription = null, 
                    modifier = Modifier.size(20.dp),
                    tint = if (isFocused) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                ) 
            },
            trailingIcon = {
                if (searchQuery.text.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = TextFieldValue("") }) {
                        Icon(Icons.Default.Close, contentDescription = "Clear", modifier = Modifier.size(18.dp))
                    }
                }
            },
            interactionSource = interactionSource,
            shape = RoundedCornerShape(16.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                cursorColor = MaterialTheme.colorScheme.primary
            )
        )

        // Filter Chips
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(listOf("All", "Work", "Personal", "Ideas", "To-do")) { filter ->
                FilterChip(
                    selected = filter == "All",
                    onClick = { },
                    label = { Text(filter, fontSize = 12.sp) },
                    shape = RoundedCornerShape(16.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = Color.White
                    ),
                    border = null
                )
            }
        }

        // Notes List
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            items(filteredNotes, key = { it.title }) { note ->
                NoteRow(note, onNoteClick)
            }
        }
    }
}

@Composable
fun NoteRow(note: DummyNote, onClick: () -> Unit) {
    val isSelected = note.title == "Product Vision 2024"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                )
                Text(
                    note.time, 
                    style = MaterialTheme.typography.labelSmall, 
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = note.preview,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

data class DummyNote(val title: String, val preview: String, val time: String)
val dummyNotes = listOf(
    DummyNote("Product Vision 2024", "The future of document collaboration is here. We need to focus on...", "10:30 AM"),
    DummyNote("Marketing Strategy", "Campaign themes: Simplicity, Speed, Security. Focus on enterprise...", "Yesterday"),
    DummyNote("Groceries", "Milk, Eggs, Bread, Avocados, Coffee beans, Pasta, Sauce...", "Feb 24"),
    DummyNote("Mobile App Feedback", "Users want more offline features and better tablet support...", "Feb 22"),
    DummyNote("Meeting Notes", "Discussed the new roadmap and sync issues with Supabase...", "Feb 20")
)
