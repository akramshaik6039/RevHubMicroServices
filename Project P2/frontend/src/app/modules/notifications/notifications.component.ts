import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { NotificationService, Notification } from '../../core/services/notification.service';

@Component({
  selector: 'app-notifications',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './notifications.component.html',
  styleUrl: './notifications.component.css'
})
export class NotificationsComponent implements OnInit {
  notifications: Notification[] = [];
  isLoading = true;

  constructor(
    private notificationService: NotificationService,
    private router: Router
  ) {}

  ngOnInit() {
    this.loadNotifications();
  }

  loadNotifications() {
    this.notificationService.getNotifications().subscribe({
      next: (notifications) => {
        this.notifications = notifications;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading notifications:', error);
        this.isLoading = false;
      }
    });
  }

  markAsRead(notification: Notification) {
    if (!notification.readStatus) {
      this.notificationService.markAsRead(notification.id).subscribe({
        next: () => {
          notification.readStatus = true;
        },
        error: (error) => {
          console.error('Error marking notification as read:', error);
        }
      });
    }
    this.navigateToNotification(notification);
  }
  
  navigateToNotification(notification: Notification) {
    if (notification.type === 'FOLLOW_REQUEST') {
      return;
    }
    
    if (notification.type === 'FOLLOW') {
      if (notification.fromUsername) {
        this.router.navigate(['/profile', notification.fromUsername]);
      }
    } else if (notification.postId) {
      this.router.navigate(['/dashboard']);
    }
  }

  acceptFollowRequest(notification: Notification) {
    if (notification.followRequestId) {
      this.notificationService.acceptFollowRequest(notification.followRequestId).subscribe({
        next: () => {
          this.loadNotifications();
        },
        error: (error) => {
          console.error('Error accepting follow request:', error);
        }
      });
    }
  }

  rejectFollowRequest(notification: Notification) {
    if (notification.followRequestId) {
      this.notificationService.rejectFollowRequest(notification.followRequestId).subscribe({
        next: () => {
          this.loadNotifications();
        },
        error: (error) => {
          console.error('Error rejecting follow request:', error);
        }
      });
    }
  }

  isFollowRequest(notification: Notification): boolean {
    return notification.type === 'FOLLOW_REQUEST';
  }

  // Helper method to get full image URL
  getImageUrl(profilePicture: string | undefined): string | null {
    if (!profilePicture) return null;
    
    // If it's already a full URL, return as is
    if (profilePicture.startsWith('http')) {
      return profilePicture;
    }
    
    // If it's a relative path, prepend backend server URL
    if (profilePicture.startsWith('/uploads/')) {
      return `http://localhost:8080${profilePicture}`;
    }
    
    // If it's just a filename, assume it's in uploads/profiles/
    return `http://localhost:8080/uploads/profiles/${profilePicture}`;
  }
}