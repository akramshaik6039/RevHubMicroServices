import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
  { path: 'dashboard', loadComponent: () => import('./dashboard.component').then(m => m.DashboardComponent), canActivate: [authGuard] },
  { path: 'profile/:username', loadComponent: () => import('./user-profile/user-profile.component').then(m => m.UserProfileComponent), canActivate: [authGuard] },
  {
    path: 'auth',
    loadChildren: () =>
      import('./modules/auth/auth.routes').then((m) => m.AUTH_ROUTES),
  },
];
