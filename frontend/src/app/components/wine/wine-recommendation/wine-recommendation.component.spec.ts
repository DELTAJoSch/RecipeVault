import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WineRecommendationComponent } from './wine-recommendation.component';

describe('WineRecommendationComponent', () => {
  let component: WineRecommendationComponent;
  let fixture: ComponentFixture<WineRecommendationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ WineRecommendationComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(WineRecommendationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
