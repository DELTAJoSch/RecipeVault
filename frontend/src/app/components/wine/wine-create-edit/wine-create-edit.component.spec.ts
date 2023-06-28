import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WineCreateEditComponent } from './wine-create-edit.component';

describe('WineCreateEditComponent', () => {
  let component: WineCreateEditComponent;
  let fixture: ComponentFixture<WineCreateEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ WineCreateEditComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(WineCreateEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
