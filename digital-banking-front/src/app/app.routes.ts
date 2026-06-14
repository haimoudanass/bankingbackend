import { Routes } from '@angular/router';
import { CustomersComponent } from './customers/customers.component';
import { NewCustomerComponent } from './new-customer/new-customer.component';
import { LoginComponent } from './login/login.component';
import { NotAuthorizedComponent } from './not-authorized/not-authorized.component';
import { AuthenticationGuard } from './guards/authentication.guard';
import { AuthorizationGuard } from './guards/authorization.guard';

export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  {
    path: 'admin',
    canActivate: [AuthenticationGuard],
    children: [
      { path: 'customers', component: CustomersComponent },
      {
        path: 'new-customer',
        component: NewCustomerComponent,
        canActivate: [AuthorizationGuard],
        data: { role: 'ADMIN' }
      },
      { path: 'notAuthorized', component: NotAuthorizedComponent }
    ]
  }
];
