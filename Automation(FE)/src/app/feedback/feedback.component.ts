import { Component } from '@angular/core';
import { FeedbackService } from '../feedback.service';
import { Feedback } from '../feedback';

@Component({
  selector: 'app-feedback',
  templateUrl: './feedback.component.html',
  styleUrl: './feedback.component.scss'
})
export class FeedbackComponent {
  
  feedbackList: Feedback[] = [];


  constructor(private feedbackService: FeedbackService) {}

  ngOnInit(): void {
    this.loadFeedback();


  //   // Refresh feedback data every 10 seconds (10000 ms)
  // setInterval(() => {
  //   this.loadFeedback();
  // }, 60000); // Adjust the interval as needed
  }

  loadFeedback(): void {
    this.feedbackService.getFeedbacks().subscribe(
      (data:any) => {
        this.feedbackList = data;
      },
      (error:any) => {
        console.error('Error fetching feedback:', error);
      }
    );
  }

}
