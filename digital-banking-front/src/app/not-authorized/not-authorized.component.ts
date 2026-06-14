import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-not-authorized',
  standalone: true,
  imports: [RouterLink],
  template: `
    <div class="container mt-5 text-center">
      <h3>Accès non autorisé</h3>
      <p class="text-muted">Vous n'avez pas les droits nécessaires pour cette page.</p>
      <a routerLink="/admin/customers" class="btn btn-primary">Retour aux clients</a>
    </div>
  `
})
export class NotAuthorizedComponent {}
