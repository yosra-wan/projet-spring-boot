package com.example.projet.projet.Model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;


@Data
@Entity
@JsonIdentityInfo(property = "id",generator = ObjectIdGenerators.PropertyGenerator.class)
@Table (name = "Services")
public class ServiceEntity {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private int id;

private String Nom;
private double Prix;
private String Description;
private String Image;



// @ManyToMany(fetch = FetchType.LAZY,
// cascade = {
//     CascadeType.PERSIST,
//     CascadeType.MERGE
// },
// mappedBy = "Services")

@ManyToOne
private CategorieEntity categorie;

@OneToMany(mappedBy = "service", targetEntity = DemandeServiceEntity.class)
List<DemandeServiceEntity> demandes_services;















}