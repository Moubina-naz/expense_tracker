import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensetracker.Room.CategoryItem

//import com.example.expensetracker.models.CategoryItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategoryDropdownGrid(
    cats: List<CategoryItem>,
    selectedCategory: CategoryItem?,
    onCategorySelected: (CategoryItem) -> Unit
) {
    var opened by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(if (opened) 180f else 0f)

    Column(modifier = Modifier.fillMaxWidth()) {
        Box {
            OutlinedTextField(
                value = selectedCategory?.name ?: "",
                label = { Text("Category", style = TextStyle(color = Color.Black),
                    fontSize = 16.sp  ) },
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth(),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        modifier = Modifier.rotate(rotation)
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Black,
                    unfocusedIndicatorColor = Color.Gray,
                    focusedLabelColor = Color.Black,
                    unfocusedLabelColor = Color.Black,
                    cursorColor = Color.Black
                )
            )

            // 🧠 Invisible overlay to handle clicks
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable { opened = !opened }
            )
        }

        // ✅ Dropdown menu
        AnimatedVisibility(
            visible = opened,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color.LightGray),
                shadowElevation = 4.dp
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .padding(8.dp)
                        .heightIn(max = 400.dp)
                ) {
                    items(cats) { item ->
                        Column(
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable {
                                    onCategorySelected(item)
                                    opened = false
                                },
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(id = item.iconRes),
                                contentDescription = item.name,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = item.name)
                        }
                    }
                }
            }
        }
    }
}
