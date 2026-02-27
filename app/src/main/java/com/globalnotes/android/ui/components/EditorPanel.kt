package com.globalnotes.android.ui.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import com.globalnotes.android.ui.theme.*

@Composable
fun EditorPanel(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    var title by remember { mutableStateOf(TextFieldValue("Product Vision 2024")) }
    var content by remember { mutableStateOf(TextFieldValue("The future of document collaboration is here. We need to focus on a truly seamless experience across all devices.\n\nKey Pillars:\n1. Performance first\n2. AI-driven drafting\n3. Beautiful typography\n\nLet's build something amazing.")) }
    var selectedBackground by remember { mutableStateOf(StylePlain) }

    val wordCount by remember {
        derivedStateOf {
            content.text.split("\\s+".toRegex()).filter { it.isNotEmpty() }.size
        }
    }

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = { EditorTopBar(onBackClick) },
        bottomBar = {
            Column {
                FormattingToolbar(onBackgroundSelect = { selectedBackground = it })
                Spacer(modifier = Modifier.navigationBarsPadding())
            }
        },
        containerColor = Color.Transparent,
        modifier = modifier.fillMaxSize().imePadding()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(selectedBackground)
        ) {
            CanvasBackground(style = selectedBackground)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                // Title Area
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    textStyle = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (selectedBackground == Color.Black) Color.White else Color.Black
                    ),
                    placeholder = { 
                        Text(
                            "Note Title", 
                            style = MaterialTheme.typography.headlineMedium, 
                            color = (if (selectedBackground == Color.Black) Color.White else Color.Black).copy(alpha = 0.3f)
                        ) 
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = if (selectedBackground == Color.Black) Color.White else MaterialTheme.colorScheme.primary
                    )
                )

                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.AccessTime, 
                        contentDescription = null, 
                        modifier = Modifier.size(12.dp), 
                        tint = if (selectedBackground == Color.Black) Color.LightGray else Color.Gray
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "Feb 27 • 12:45 PM", 
                        style = MaterialTheme.typography.labelSmall, 
                        color = if (selectedBackground == Color.Black) Color.LightGray else Color.Gray
                    )
                }

                Divider(
                    modifier = Modifier.padding(vertical = 16.dp),
                    color = (if (selectedBackground == Color.Black) Color.White else Color.Black).copy(alpha = 0.1f)
                )

                // Body text
                TextField(
                    value = content,
                    onValueChange = { content = it },
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        lineHeight = 26.sp,
                        color = if (selectedBackground == Color.Black) Color.White else Color.Black
                    ),
                    placeholder = { 
                        Text(
                            "Start writing...", 
                            color = (if (selectedBackground == Color.Black) Color.White else Color.Black).copy(alpha = 0.3f)
                        ) 
                    },
                    modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = if (selectedBackground == Color.Black) Color.White else MaterialTheme.colorScheme.primary
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "$wordCount words",
                    style = MaterialTheme.typography.labelSmall,
                    color = (if (selectedBackground == Color.Black) Color.White else Color.Black).copy(alpha = 0.4f),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}

@Composable
fun EditorTopBar(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().height(64.dp).padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back", modifier = Modifier.size(18.dp)) }
            Text("Editor", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
        }
        Row {
            IconButton(onClick = { }) { Icon(Icons.Outlined.Share, contentDescription = "Share") }
            IconButton(onClick = { }) { Icon(Icons.Outlined.ContentCopy, contentDescription = "Duplicate") }
            IconButton(onClick = { }) { Icon(Icons.Outlined.Delete, contentDescription = "Delete") }
            IconButton(onClick = { }) { Icon(Icons.Default.MoreVert, contentDescription = "More") }
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp),
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("AI", fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun FormattingToolbar(onBackgroundSelect: (Color) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .border(0.5.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        LazyRow(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Format Group
            item { ToolbarGroup(listOf(Icons.Default.FormatBold, Icons.Default.FormatItalic, Icons.Default.FormatUnderlined)) }
            item { Divider(modifier = Modifier.height(24.dp).width(1.dp)) }
            // Alignment
            item { ToolbarGroup(listOf(Icons.Default.FormatAlignLeft, Icons.Default.FormatAlignCenter, Icons.Default.FormatAlignRight)) }
            item { Divider(modifier = Modifier.height(24.dp).width(1.dp)) }
            // Colors
            item { ColorSwatches() }
            item { Divider(modifier = Modifier.height(24.dp).width(1.dp)) }
            // Background Styles
            item { BackgroundSwatches(onBackgroundSelect) }
        }
    }
}

@Composable
fun ToolbarGroup(icons: List<ImageVector>) {
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        icons.forEach { icon ->
            IconButton(onClick = { }, modifier = Modifier.size(32.dp)) {
                Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp), tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
            }
        }
    }
}

@Composable
fun ColorSwatches() {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf(Color.Black, Color.Red, Color.Blue, Color.Green, Color.Gray).forEach { color ->
            Box(modifier = Modifier.size(20.dp).clip(CircleShape).background(color).clickable { })
        }
    }
}

@Composable
fun BackgroundSwatches(onSelect: (Color) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf(StylePlain, StyleLined, StyleGrid, StyleSunset, StyleMint).forEach { color ->
            Box(
                modifier = Modifier
                    .size(24.dp, 32.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(color)
                    .border(0.5.dp, Color.LightGray, RoundedCornerShape(4.dp))
                    .clickable { onSelect(color) }
            )
        }
    }
}

@Composable
fun CanvasBackground(style: Color) {
    // Placeholder for actual Canvas drawing (lines, grid)
    // We can use a pattern background or draw directly on a Canvas
}
