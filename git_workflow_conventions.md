# Git Workflow y Convenciones de Desarrollo

Este documento define el flujo de trabajo que debe seguir cualquier
desarrollador que contribuya al repositorio.

El objetivo es: - Mantener un historial limpio. - Evitar conflictos
entre desarrolladores. - Proteger la rama principal. - Facilitar
revisiones de código.

------------------------------------------------------------------------

# 1. Estructura de ramas

El repositorio usa una rama principal:

-   `main` → código estable listo para producción.

Todas las nuevas funcionalidades o cambios deben desarrollarse en
**ramas secundarias (feature branches)**.

Ejemplo:

    main
     ├─ feat/user-login
     ├─ feat/payment-api
     ├─ fix/auth-bug
     └─ refactor/user-service

------------------------------------------------------------------------

# 2. Convención de nombres de ramas

Usar el siguiente formato:

    tipo/descripcion-corta

Tipos permitidos:

    feat/       Nueva funcionalidad
    fix/        Corrección de bug
    refactor/   Cambio interno sin alterar funcionalidad
    docs/       Documentación
    chore/      Mantenimiento
    test/       Pruebas

Ejemplos:

    feat/user-registration
    fix/jwt-expiration
    refactor/payment-service

------------------------------------------------------------------------

# 3. Flujo de trabajo estándar

## Paso 1 --- Actualizar repositorio

    git checkout main
    git pull origin main

------------------------------------------------------------------------

## Paso 2 --- Crear una nueva rama

    git checkout -b feat/nombre-feature

Ejemplo:

    git checkout -b feat/user-login

------------------------------------------------------------------------

## Paso 3 --- Realizar cambios

Realizar commits pequeños y claros.

Ejemplo:

    git commit -m "feat: add login endpoint"
    git commit -m "feat: add password validation"

------------------------------------------------------------------------

## Paso 4 --- Subir la rama al repositorio

    git push origin feat/user-login

------------------------------------------------------------------------

## Paso 5 --- Crear Pull Request

Usando GitHub CLI:

    gh pr create --base main --head feat/user-login

Con título y descripción:

    gh pr create --title "feat: user login endpoint" --body "Adds login endpoint with JWT authentication"

------------------------------------------------------------------------

# 4. Revisión de código

Todo cambio debe pasar por **Pull Request y revisión** antes de ser
integrado.

Flujo:

    feature branch → Pull Request → Review → Merge

El revisor verificará:

-   lógica del código
-   seguridad
-   claridad
-   posibles errores

------------------------------------------------------------------------

# 5. Merge

Se utiliza **squash merge** para mantener historial limpio.

Con GitHub CLI:

    gh pr merge --squash

------------------------------------------------------------------------

# 6. Eliminar ramas después del merge

Después de integrar el cambio:

    git branch -d feat/user-login
    git push origin --delete feat/user-login

------------------------------------------------------------------------

# 7. Convención de commits

Formato:

    tipo: descripcion

Ejemplos:

    feat: add login endpoint
    fix: correct JWT expiration bug
    refactor: simplify user service
    docs: update API documentation

------------------------------------------------------------------------

# 8. Buenas prácticas

-   No hacer push directo a `main`
-   Usar ramas para cada cambio
-   Crear Pull Request para integración
-   Escribir commits claros
-   Mantener ramas actualizadas con `main`

Actualizar rama:

    git fetch origin
    git rebase origin/main

------------------------------------------------------------------------

# 9. Resumen del flujo

    main
      ↓
    crear rama
      ↓
    desarrollar feature
      ↓
    push
      ↓
    Pull Request
      ↓
    review
      ↓
    merge

------------------------------------------------------------------------

# 10. Principios del repositorio

-   Cambios pequeños y frecuentes
-   Revisión de código obligatoria
-   Historial limpio
-   Seguridad en la rama principal
