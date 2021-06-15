import { registerLocaleData } from '@angular/common';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { RegistarComponent } from './registar/registar.component';
import { ResetPasswordComponent } from './reset-password/reset-password.component';
import { UsersTableComponent } from './users-table/users-table.component';


const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'registar', component: RegistarComponent }, 
  { path: 'reset-password', component: ResetPasswordComponent }, 
  { path: 'users-table', component: UsersTableComponent }, 
  { path: '**', redirectTo:'/login'  } // Wildcard Route
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
