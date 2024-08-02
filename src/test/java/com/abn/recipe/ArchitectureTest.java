package com.abn.recipe;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

@AnalyzeClasses(packages = "com.abn.recipe..", importOptions = ImportOption.DoNotIncludeTests.class)
class ArchitectureTest {
    final static String apiLayer = "Api";
    final static String domainLayer = "Domain";
    final static String serviceLayer = "Service";
    final static String repositoryLayer = "Repository";

    @ArchTest
    static final ArchRule layer_dependencies_are_respected = layeredArchitecture()
            .consideringAllDependencies()
            .layer(apiLayer).definedBy("com.abn.recipe.api..")
            .layer(domainLayer).definedBy("com.abn.recipe.domain..")
            .layer(repositoryLayer).definedBy("com.abn.recipe.repository..")
            .layer(serviceLayer).definedBy("com.abn.recipe.service..")

            .whereLayer(apiLayer).mayNotBeAccessedByAnyLayer()
            .whereLayer(repositoryLayer).mayOnlyBeAccessedByLayers(serviceLayer, domainLayer)
            .whereLayer(serviceLayer).mayOnlyBeAccessedByLayers(apiLayer);
}
