import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ActiveDeliveriesComponent } from './active-deliveries.component';

describe('ActiveDeliveriesComponent', () => {
  let component: ActiveDeliveriesComponent;
  let fixture: ComponentFixture<ActiveDeliveriesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ActiveDeliveriesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ActiveDeliveriesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
