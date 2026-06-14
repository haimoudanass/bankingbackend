/**
 * Equivalent du tutoriel Youssfi (NgModule).
 * Avec Angular 17 standalone, le routage actif est dans app.routes.ts.
 */
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CustomersComponent } from './customers/customers.component';
import { NewCustomerComponent } from './new-customer/new-customer.component';

const routes: Routes = [
  { path: '', redirectTo: '/customers', pathMatch: 'full' },
  { path: 'customers', component: CustomersComponent },
  { path: 'new-customer', component: NewCustomerComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
