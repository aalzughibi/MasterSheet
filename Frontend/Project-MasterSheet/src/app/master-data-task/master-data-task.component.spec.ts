import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MasterDataTaskComponent } from './master-data-task.component';

describe('MasterDataTaskComponent', () => {
  let component: MasterDataTaskComponent;
  let fixture: ComponentFixture<MasterDataTaskComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MasterDataTaskComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MasterDataTaskComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
