import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';


import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { ResetPasswordComponent } from './reset-password/reset-password.component';
import { UsersTableComponent } from './users-table/users-table.component';
import { HeaderComponent } from './header/header.component';
import { FooterComponent } from './footer/footer.component';
import { MasterDataComponent } from './master-data/master-data.component';
import { MasterDataItemComponent } from './master-data-item/master-data-item.component';
import { MasterDataTaskComponent } from './master-data-task/master-data-task.component';
import { MasterDataPoComponent } from './master-data-po/master-data-po.component';
import { RegisterComponent } from './register/register.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    ResetPasswordComponent,
    UsersTableComponent,
    HeaderComponent,
    FooterComponent,
    MasterDataComponent,
    MasterDataItemComponent,
    MasterDataTaskComponent,
    MasterDataPoComponent,
    RegisterComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
