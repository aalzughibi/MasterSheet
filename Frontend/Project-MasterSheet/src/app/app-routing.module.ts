import { registerLocaleData } from '@angular/common';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { FooterComponent } from './footer/footer.component';
import { HeaderComponent } from './header/header.component';
import { HistoryPageComponent } from './history-page/history-page.component';
import { LoginComponent } from './login/login.component';
import { MasterDataItemComponent } from './master-data-item/master-data-item.component';
import { MasterDataPoComponent } from './master-data-po/master-data-po.component';
import { MasterDataTaskComponent } from './master-data-task/master-data-task.component';
import { MasterDataComponent } from './master-data/master-data.component';
import { RegisterComponent } from './register/register.component';
import { ResetPasswordComponent } from './reset-password/reset-password.component';
import { UsersTableComponent } from './users-table/users-table.component';


const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent }, 
  { path: 'reset-password', component: ResetPasswordComponent }, 
  { path: 'users-table', component: UsersTableComponent }, 
  { path: 'header', component: HeaderComponent }, 
  { path: 'footer', component: FooterComponent }, 
  { path: 'master-data', component: MasterDataComponent }, 
  { path: 'master-data-item', component: MasterDataItemComponent }, 
  { path: 'master-data-task', component: MasterDataTaskComponent }, 
  { path: 'master-data-po', component: MasterDataPoComponent },  
  { path: 'history-page', component: HistoryPageComponent },  //delete
  { path: '**', redirectTo:'/login'  } // Wildcard Route
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
