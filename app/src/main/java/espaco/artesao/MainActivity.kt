package espaco.artesao

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import espaco.artesao.ui.theme.ArtesaoTheme

enum class AppScreen { Home, Cart, Profile, Dashboard, Orders }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ArtesaoTheme {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp() {
    var currentScreen by remember { mutableStateOf(AppScreen.Home) }

    Scaffold(
        topBar = {
            ArtesaoTopAppBar(
                currentScreen = currentScreen,
                onNavigate = { currentScreen = it },
                onBackClick = if (currentScreen != AppScreen.Home) { { currentScreen = AppScreen.Home } } else null
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (currentScreen) {
                AppScreen.Home -> HomeScreen(
                    onProductClick = { currentScreen = AppScreen.Profile }
                )
                AppScreen.Cart -> CartScreen(
                    onContinueShoppingClick = { currentScreen = AppScreen.Home }
                )
                AppScreen.Profile -> ProfileScreen()
                AppScreen.Dashboard -> ArtisanDashboardScreen()
                AppScreen.Orders -> OrdersScreen(
                    onExploreProductsClick = { currentScreen = AppScreen.Home }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtesaoTopAppBar(
    currentScreen: AppScreen,
    onNavigate: (AppScreen) -> Unit,
    onBackClick: (() -> Unit)? = null
) {
    var menuExpanded by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Column {
                Text(
                    text = when(currentScreen) {
                        AppScreen.Home -> "Espaço do Artesão"
                        AppScreen.Cart -> "Meu Carrinho"
                        AppScreen.Orders -> "Meus Pedidos"
                        AppScreen.Profile -> "Perfil"
                        AppScreen.Dashboard -> "Painel do Artesão"
                    },
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                if (currentScreen == AppScreen.Home) {
                    Text(
                        text = "Feito à mão, com amor",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        navigationIcon = {
            if (onBackClick != null) {
                TextButton(onClick = onBackClick) {
                    Text("Voltar")
                }
            }
        },
        actions = {
            Box {
                TextButton(onClick = { menuExpanded = true }) {
                    Text("Menu")
                }
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false },
                    modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                ) {
                    DropdownMenuItem(text = { Text("Início") }, onClick = { menuExpanded = false; onNavigate(AppScreen.Home) })
                    DropdownMenuItem(text = { Text("Meu Carrinho") }, onClick = { menuExpanded = false; onNavigate(AppScreen.Cart) })
                    DropdownMenuItem(text = { Text("Meus Pedidos") }, onClick = { menuExpanded = false; onNavigate(AppScreen.Orders) })
                    DropdownMenuItem(text = { Text("Perfil") }, onClick = { menuExpanded = false; onNavigate(AppScreen.Profile) })
                    DropdownMenuItem(text = { Text("Painel do Artesão") }, onClick = { menuExpanded = false; onNavigate(AppScreen.Dashboard) })
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}

@Composable
fun HomeScreen(onProductClick: () -> Unit = {}) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            HeroSection()
        }
        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
        item {
            ProductsHeader()
        }
        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
        
        val dummyProducts = (1..6).toList()
        val chunkedProducts = dummyProducts.chunked(2)
        
        items(chunkedProducts) { rowItems ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                for (item in rowItems) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onProductClick() }
                    ) {
                        ProductCard()
                    }
                }
                if (rowItems.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun HeroSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(horizontal = 24.dp, vertical = 48.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                color = MaterialTheme.colorScheme.surface
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "Buscar artesanato...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "Q",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Descubra o Artesanato Brasileiro",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Conectamos você diretamente a artesãos locais. Cada peça conta uma história única.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(50.dp)
            ) {
                Text(
                    text = "Explorar Produtos",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@Composable
fun ProductsHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Produtos Artesanais",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Explore nossa coleção de produtos únicos feitos por artesãos locais.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Categorias",
                    modifier = Modifier.padding(vertical = 12.dp, horizontal = 12.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelMedium
                )
            }
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Filtrar",
                    modifier = Modifier.padding(vertical = 12.dp, horizontal = 12.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Composable
fun ProductCard() {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Text(
                    text = "♡",
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = "Vaso de Cerâmica",
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "por João Silva",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "R$ 89,90",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    ArtesaoTheme {
        HomeScreen()
    }
}
