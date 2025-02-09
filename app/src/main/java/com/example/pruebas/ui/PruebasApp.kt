package com.example.pruebas.ui

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.pruebas.R
import com.example.pruebas.modelo.DrawerMenu
import com.example.pruebas.modelo.Ruta
import com.example.pruebas.ui.pantallas.Ajustes
import com.example.pruebas.ui.pantallas.Idiomas
import com.example.pruebas.ui.pantallas.Inicio
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

enum class Pantallas(@StringRes val titulo: Int){
    Inicio(titulo = R.string.inicio),
    Ajustes(titulo = R.string.ajustes),
    Idiomas(titulo = R.string.idiomas)
}

val listaRutas = listOf(
    Ruta(Pantallas.Ajustes.titulo, Pantallas.Ajustes.name, Icons.Filled.Place, Icons.Outlined.Place),
    Ruta(Pantallas.Inicio.titulo, Pantallas.Inicio.name, Icons.Filled.Home, Icons.Outlined.Home),
    Ruta(Pantallas.Idiomas.titulo, Pantallas.Idiomas.name, Icons.Filled.Info, Icons.Outlined.Info)
)

val menu = arrayOf(
    DrawerMenu(Icons.Filled.Home, Pantallas.Inicio.titulo, Pantallas.Inicio.name),
    DrawerMenu(Icons.Filled.Settings, Pantallas.Ajustes.titulo, Pantallas.Ajustes.name),
    DrawerMenu(Icons.Filled.Place, Pantallas.Idiomas.titulo, Pantallas.Idiomas.name)
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PruebasApp(
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
){
    var selectedItem by remember { mutableIntStateOf(1) }

    val pilaRetroceso by navController.currentBackStackEntryAsState()

    val pantallaActual = Pantallas.valueOf(
        pilaRetroceso?.destination?.route ?: Pantallas.Inicio.name
    )

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    ModalNavigationDrawer(  // --> Esto envuelve todo el scaffold
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerContent(
                    menu = menu,
                    pantallaActual = pantallaActual
                ) { ruta ->
                    coroutineScope.launch {
                        drawerState.close()
                    }


                    navController.navigate(ruta)
                }
            }
        },
    ) { // --> Desde aquí
        Scaffold(
            topBar = {
                AppTopBar(
                    pantallaActual = pantallaActual,
                    navController = navController,
                    scrollBehavior = scrollBehavior,
                    drawerState = drawerState
                )
            },
            // BOTTOM BAR INICIO
            bottomBar = {
                NavigationBar {
                    listaRutas.forEachIndexed { indice, ruta ->
                        NavigationBarItem(
                            icon = {
                                if (selectedItem == indice)
                                    Icon(
                                        imageVector = ruta.iconoLleno,
                                        contentDescription = stringResource(id = ruta.nombre)
                                    )
                                else
                                    Icon(
                                        imageVector = ruta.iconoVacio,
                                        contentDescription = stringResource(id = ruta.nombre)
                                    )
                            },
                            label = { Text(stringResource(id = ruta.nombre)) },
                            selected = selectedItem == indice,
                            onClick = {
                                selectedItem = indice
                                navController.navigate(ruta.ruta)
                            }
                        )
                    }
                }
            }, // BOTTOM BAR FINAL
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
        ) { innerPadding ->

            NavHost(
                navController = navController,
                startDestination = Pantallas.Inicio.name,
                modifier = Modifier.padding(innerPadding)
            ) {
                // Grafo de las rutas
                composable(route = Pantallas.Inicio.name) {
                    Inicio(

                    )
                }
                composable(route = Pantallas.Ajustes.name) {
                    Ajustes(

                    )
                }
                composable(route = Pantallas.Idiomas.name) {
                    Idiomas(

                    )
                }
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    pantallaActual: Pantallas,
    navController: NavHostController,
    scrollBehavior: TopAppBarScrollBehavior,
    drawerState: DrawerState?,

    ){
    var mostrarMenu by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    TopAppBar(
        title = { Text(text = stringResource(id = pantallaActual.titulo)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),

        navigationIcon = {
            if (drawerState != null) {
                if(true){
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                drawerState.open()
                            }
                        }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = stringResource(id = R.string.atr_s)
                        )
                    }
                }
            }
        },
        actions = {
            if(true) {
                IconButton(onClick = { mostrarMenu = true }) {
                    Icon(
                        imageVector = Icons.Outlined.MoreVert,
                        contentDescription = stringResource(R.string.abrir_men)
                    )
                }
                DropdownMenu(
                    expanded = mostrarMenu,
                    onDismissRequest = { mostrarMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(text = stringResource(id = R.string.ajustes)) },
                        onClick = {
                            mostrarMenu = false
                            navController.navigate(Pantallas.Ajustes.name)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text = stringResource(R.string.idiomas)) },
                        onClick = {
                            mostrarMenu = false
                            navController.navigate(Pantallas.Idiomas.name)
                        }
                    )
                }
            }
        },
        scrollBehavior = scrollBehavior,
    )
}

@Composable
private fun DrawerContent(
    menu: Array<DrawerMenu>,
    pantallaActual: Pantallas,
    onMenuClick: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier.size(150.dp),
                imageVector = Icons.Filled.AccountCircle,
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        menu.forEach {
            NavigationDrawerItem(
                label = { Text(text = stringResource(id = it.titulo)) },
                icon = { Icon(imageVector = it.icono, contentDescription = null) },
                selected = it.titulo == pantallaActual.titulo,
                onClick = {
                    onMenuClick(it.ruta)
                }
            )
        }
    }
}