import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FavoriteOverviewComponent } from './favorite-overview.component';

describe('FavoriteOverviewComponent', () => {
  let component: FavoriteOverviewComponent;
  let fixture: ComponentFixture<FavoriteOverviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FavoriteOverviewComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FavoriteOverviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
