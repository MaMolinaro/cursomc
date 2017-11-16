package com.molinaro.cursomc.repositories;



import org.springframework.stereotype.Repository;

import com.molinaro.cursomc.domain.Categoria;

@Repository
public class CategoriaRepository extends JpaRepository<Categoria, Integer> {

}
