package com.ud.myapplication.persistence

object Tablero {
    private var tablero: MutableList<MutableList<Casilla>> = mutableListOf()

    fun iniciarTablero(): List<List<Casilla>>{
        if(tablero.isEmpty()){
            val rows = 8
            val columns = 8
            var acum = 1;
            for(i in 0 until rows){
                val filasCasillas = mutableListOf<Casilla>()
                for(j in 0 until columns){
                    when(acum){
                        //LINE SNAKE
                        3,  5, 22, 43 -> {
                            filasCasillas.add(Casilla(acum, EnumEstado.LINE.toString()))
                        }
                        //HEAD SNAKE
                        31, 41, 51, 60 -> {
                            filasCasillas.add(Casilla(acum, EnumEstado.HEAD.toString()))
                        }
                        //UP Ladders
                        8, 12, 32, 44 -> {
                            filasCasillas.add(Casilla(acum, EnumEstado.UP.toString()))
                        }
                        //DOWN Ladders
                        38, 39, 47, 61 -> {
                            filasCasillas.add(Casilla(acum, EnumEstado.DOWN.toString()))
                        }
                        else -> {
                            //Default case
                            filasCasillas.add(Casilla(acum))
                        }
                    }
                    acum++
                }
                tablero.add(filasCasillas)
            }
        }
        return tablero;
    }

}