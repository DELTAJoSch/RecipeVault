import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HeaderEntryComponent } from './header-entry.component';

describe('HeaderEntryComponent', () => {
  let component: HeaderEntryComponent;
  let fixture: ComponentFixture<HeaderEntryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ HeaderEntryComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HeaderEntryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
