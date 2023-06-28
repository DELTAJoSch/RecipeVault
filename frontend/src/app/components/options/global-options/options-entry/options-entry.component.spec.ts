import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OptionsEntryComponent } from './options-entry.component';

describe('OptionsEntryComponent', () => {
  let component: OptionsEntryComponent;
  let fixture: ComponentFixture<OptionsEntryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OptionsEntryComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OptionsEntryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
