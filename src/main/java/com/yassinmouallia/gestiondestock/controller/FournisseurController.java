package com.yassinmouallia.gestiondestock.controller;

import com.yassinmouallia.gestiondestock.controller.api.FournisseurApi;
import com.yassinmouallia.gestiondestock.dto.FournisseurDto;
import com.yassinmouallia.gestiondestock.services.FournisseurService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FournisseurController implements FournisseurApi {

  private FournisseurService fournisseurService;

  @Autowired
  public FournisseurController(FournisseurService fournisseurService) {
    this.fournisseurService = fournisseurService;
  }

  @Override
  public FournisseurDto save(FournisseurDto dto) {
    return fournisseurService.save(dto);
  }

  @Override
  public FournisseurDto findById(Integer id) {
    return fournisseurService.findById(id);
  }

  @Override
  public List<FournisseurDto> findAll() {
    return fournisseurService.findAll();
  }

  @Override
  public void delete(Integer id) {
    fournisseurService.delete(id);
  }
}
