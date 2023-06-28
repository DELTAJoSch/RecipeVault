import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CommentOverviewComponent } from './comment-overview.component';

describe('CommentOverviewComponent', () => {
  let component: CommentOverviewComponent;
  let fixture: ComponentFixture<CommentOverviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CommentOverviewComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CommentOverviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
